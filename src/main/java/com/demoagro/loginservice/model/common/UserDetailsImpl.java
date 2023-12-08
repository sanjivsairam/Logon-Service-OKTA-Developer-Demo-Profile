package com.demoagro.loginservice.model.common;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.demoagro.loginservice.model.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.demoagro.loginservice.constants.LoginServiceConstants;

public class UserDetailsImpl implements UserDetails {
	private static final long serialVersionUID = 1L;
	
	private Long userId;

	@JsonIgnore
	private String username;

	@JsonIgnore
	private String password;

	private String name;

	private String email;

	private String profile;

	private String jobTitle;

	private boolean notifications;

	private Collection<? extends GrantedAuthority> authorities;

	/**
	 * @param userId
	 * @param username
	 * @param password
	 * @param name
	 * @param email
	 * @param profile
	 * @param jobTitle
	 * @param notifications
	 * @param authorities
	 */
	public UserDetailsImpl(Long userId, String username, String password, String name, String email, String profile,
			String jobTitle, boolean notifications, Collection<? extends GrantedAuthority> authorities) {
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.name = name;
		this.email = email;
		this.profile = profile;
		this.jobTitle = jobTitle;
		this.notifications = notifications;
		this.authorities = authorities;

	}

	/**
	 * @param user
	 * @return
	 */
	public static UserDetailsImpl build(User user) {
		List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority(LoginServiceConstants.ROLE + user.getProfile()));

		return new UserDetailsImpl(user.getUserId(), user.getUserName(), user.getPassword(), user.getName(), user.getEmail(),
				user.getProfile(), user.getJobTitle(), user.isNotifications(), authorities);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public Long getUserId() {
		return userId;
	}

	public String getEmail() {
		return email;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the profile
	 */
	public String getProfile() {
		return profile;
	}

	/**
	 * @return the jobTitle
	 */
	public String getJobTitle() {
		return jobTitle;
	}

	/**
	 * @return the notifications
	 */
	public boolean isNotifications() {
		return notifications;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
