package com.example.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	
	//serviceクラスの＠Bean定義必要
	@Autowired
	private UserDetailsService userDetailsService;
	
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder(){
		//パスワードBCryptで暗号化するメソッド
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bCryptPasswordEncoder;
	}
	@Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                            "/images/**",
                            "/css/**",
                            "/javascript/**"
                            );
    }

	
	@Override
	public void configure(HttpSecurity http) throws Exception{
		http
			.authorizeRequests()
				.anyRequest().authenticated()
				.and()
			.formLogin()
				.loginPage("/login")//ログインページはコントローラーを経由しないので、ViewNameとの紐付けが必要
				.loginProcessingUrl("/sign_in")//指定したURLがリクエストを受けたら、認証の処理が始まる合図
				.usernameParameter("username")//ログイン画面のユーザ名のパラメータ名
				.passwordParameter("password")//ログイン画面のパスワードのパラメータ名
				.defaultSuccessUrl("/hello")//ログイン成功時のアクセスするURL
				.failureUrl("/login?error")//ログインが失敗した時のURL
				.permitAll()//全てのアクセスを許可
				
				.and()
			.logout()//ログアウト設定
				.logoutUrl("/logout")//ログアウト失敗時のURL
				.logoutSuccessUrl("/login?logout")//ログアウト成功時のURL
				.permitAll();
	}
	@Override
	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
	
	
	
}
