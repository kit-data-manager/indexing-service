/*
 * Copyright 2018 Karlsruhe Institute of Technology.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.kit.datamanager.indexer.configuration;

import edu.kit.datamanager.security.filter.KeycloakTokenFilter;
import edu.kit.datamanager.security.filter.NoAuthenticationFilter;
import java.util.Optional;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 *
 * @author jejkal
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

  @Autowired
  private Logger logger;

  @Autowired
  private ApplicationProperties applicationProperties;

  @Autowired
  private Optional<KeycloakTokenFilter> keycloaktokenFilterBean;

  public WebSecurityConfig(){
  }

 
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    AuthenticationManager authenticationManager = authenticationManager(http.getSharedObject(AuthenticationConfiguration.class));
    HttpSecurity httpSecurity = http.authorizeRequests()
            .antMatchers(HttpMethod.OPTIONS, "/**").permitAll().and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .csrf().disable();
    
    if (keycloaktokenFilterBean.isPresent()) {
      logger.info("Add keycloak filter!");
      httpSecurity.addFilterAfter(keycloaktokenFilterBean.get(), BasicAuthenticationFilter.class);
    }
    if (!applicationProperties.isAuthEnabled()) {
      logger.info("Authentication is DISABLED. Adding 'NoAuthenticationFilter' to authentication chain.");
      httpSecurity = httpSecurity.addFilterAfter(new NoAuthenticationFilter(applicationProperties.getJwtSecret(), authenticationManager), BasicAuthenticationFilter.class);
    } else {
      logger.info("Authentication is ENABLED.");
    }

    httpSecurity.
            authorizeRequests().
            antMatchers("/api/v1").authenticated();

    http.headers().cacheControl().disable();

    return http.build();
  }

  @Bean
  public HttpFirewall allowUrlEncodedSlashHttpFirewall(){
    DefaultHttpFirewall firewall = new DefaultHttpFirewall();
    firewall.setAllowUrlEncodedSlash(true);
    return firewall;
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> web.httpFirewall(allowUrlEncodedSlashHttpFirewall());
  }

  @Bean
  public FilterRegistrationBean corsFilter(){
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.addAllowedOrigin("*"); // @Value: http://localhost:8080
    config.addAllowedHeader("*");
    config.addAllowedMethod("*");
    config.addExposedHeader("Content-Range");
    config.addExposedHeader("ETag");

    source.registerCorsConfiguration("/**", config);
    FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
    bean.setOrder(0);
    return bean;
  }

}
