package com.example.app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.app.dao.LoginUserDao;
import com.example.app.entity.LoginUser;

@Service
public class UserDetailsServiceImpl  implements UserDetailsService{
	
	//DBからユーザ情報を検索するメソッドを実行したクラス
	@Autowired
	private LoginUserDao userDao;
	
	@Override
	public UserDetails loadUserByUsername(String userName)throws UsernameNotFoundException{
		
		LoginUser user = userDao.findUser(userName);
		
		if(user == null) {
			throw new UsernameNotFoundException("User" + userName + "was not found in the database");
		}
		
		//権限のリスト
		List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
		GrantedAuthority authority = new SimpleGrantedAuthority("USER");
		grantList.add(authority);
		
		//rawDataのパスワードは渡すことができないので、暗号化
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		//UserDetailsはインターフェイスなのでUserクラス
		UserDetails userDetails = (UserDetails)new User(user.getUserName(),encoder.encode(user.getPassword()),grantList);
		return userDetails;
	}

}
