package com.github.thushear.config;


import com.github.thushear.security.*;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.jasig.cas.client.validation.Saml11TicketValidator;
import org.springframework.web.filter.CorsFilter;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    private static final String CAS_URL_LOGIN = "cas.service.login";
	private static final String CAS_URL_LOGOUT = "cas.service.logout";
	private static final String CAS_URL_PREFIX = "cas.url.prefix";
	private static final String CAS_SERVICE_URL = "app.service.security";
	private static final String APP_SERVICE_HOME = "app.service.home";
	private static final String APP_ADMIN_USER_NAME = "app.admin.userName";

	@Inject
	private Environment env;

	 @Inject private AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler;

	  @Inject private AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler;

	  @Inject private AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler;

	 @Inject
	 private CasAuthenticationProvider authProvider;

	@Bean
	public Set<String> adminList() {
		Set<String> admins = new HashSet<String>();
		String adminUserName = env.getProperty(APP_ADMIN_USER_NAME);

		admins.add("admin");
		if (adminUserName != null && !adminUserName.isEmpty()) {
			admins.add(adminUserName);
		}
		return admins;
	}

	@Bean
	public ServiceProperties serviceProperties() {
		ServiceProperties sp = new ServiceProperties();
		sp.setService(env.getRequiredProperty(CAS_SERVICE_URL));
		sp.setSendRenew(false);
		return sp;
	}

	@Bean
	public CasAuthenticationProvider casAuthenticationProvider() {
		CasAuthenticationProvider casAuthenticationProvider = new CasAuthenticationProvider();
		casAuthenticationProvider.setAuthenticationUserDetailsService(customUserDetailsService());
		casAuthenticationProvider.setServiceProperties(serviceProperties());
		casAuthenticationProvider.setTicketValidator(cas20ServiceTicketValidator());
		casAuthenticationProvider.setKey("an_id_for_this_auth_provider_only");
		return casAuthenticationProvider;
	}

	@Bean
	public AuthenticationUserDetailsService<CasAssertionAuthenticationToken> customUserDetailsService() {
		return new CustomUserDetailsService(adminList());
	}

	@Bean
	public SessionAuthenticationStrategy sessionStrategy() {
		SessionAuthenticationStrategy sessionStrategy = new SessionFixationProtectionStrategy();
		return sessionStrategy;
	}

	@Bean
	public Saml11TicketValidator casSamlServiceTicketValidator() {
		return new Saml11TicketValidator(env.getRequiredProperty(CAS_URL_PREFIX));
	}

	@Bean
	public Cas20ServiceTicketValidator cas20ServiceTicketValidator() {
		return new Cas20ServiceTicketValidator(env.getRequiredProperty(CAS_URL_PREFIX));
	}

	@Bean
	public CasAuthenticationFilter casAuthenticationFilter() throws Exception {
		CasAuthenticationFilter casAuthenticationFilter = new CasAuthenticationFilter();
		casAuthenticationFilter.setAuthenticationManager(authenticationManager());
		casAuthenticationFilter.setSessionAuthenticationStrategy(sessionStrategy());
		return casAuthenticationFilter;
	}

	@Bean
	public CasAuthenticationEntryPoint casAuthenticationEntryPoint() {
		CasAuthenticationEntryPoint casAuthenticationEntryPoint = new CasAuthenticationEntryPoint();
		casAuthenticationEntryPoint.setLoginUrl(env.getRequiredProperty(CAS_URL_LOGIN));
		casAuthenticationEntryPoint.setServiceProperties(serviceProperties());
		return casAuthenticationEntryPoint;
	}

	@Bean
	public SingleSignOutFilter singleSignOutFilter() {
		SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();
		singleSignOutFilter.setCasServerUrlPrefix(env.getRequiredProperty(CAS_URL_PREFIX));
		return singleSignOutFilter;
	}

	@Bean
	public LogoutFilter requestCasGlobalLogoutFilter() {
		LogoutFilter logoutFilter = new LogoutFilter(env.getRequiredProperty(CAS_URL_LOGOUT) + "?service="
				+ env.getRequiredProperty(APP_SERVICE_HOME), new SecurityContextLogoutHandler());
		// logoutFilter.setFilterProcessesUrl("/logout");
		// logoutFilter.setFilterProcessesUrl("/j_spring_cas_security_logout");
		logoutFilter.setLogoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST"));
		return logoutFilter;
	}

	@Inject
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(casAuthenticationProvider());
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/fonts/**").antMatchers("/images/**").antMatchers("/scripts/**")
				.antMatchers("/styles/**").antMatchers("/views/**").antMatchers("/i18n/**");
	}



	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.exceptionHandling()
				.authenticationEntryPoint(casAuthenticationEntryPoint()).and().addFilter(casAuthenticationFilter())
				.addFilterBefore(singleSignOutFilter(), CasAuthenticationFilter.class)
				.addFilterBefore(requestCasGlobalLogoutFilter(), LogoutFilter.class)
                .headers()
            .frameOptions()
            .disable()
            .and()
            .authorizeRequests()
            .antMatchers("/api/register").permitAll()
            .antMatchers("/api/activate").permitAll()
            .antMatchers("/api/authenticate").permitAll()
            .antMatchers("/api/account/reset-password/init").permitAll()
            .antMatchers("/api/account/reset-password/finish").permitAll()
            .antMatchers("/api/profile-info").permitAll()
            .antMatchers("/api/**").authenticated()
            .antMatchers("/management/health").permitAll()
            .antMatchers("/management/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/v2/api-docs/**").permitAll()
            .antMatchers("/swagger-resources/configuration/ui").permitAll()
            .antMatchers("/swagger-ui/index.html").hasAuthority(AuthoritiesConstants.ADMIN);


		/**
		 * <logout invalidate-session="true" delete-cookies="JSESSIONID" />
		 */
		http.logout().logoutUrl("/logout").logoutSuccessUrl("/").invalidateHttpSession(true)
				.deleteCookies("JSESSIONID");

		// http.csrf();
	}
}
