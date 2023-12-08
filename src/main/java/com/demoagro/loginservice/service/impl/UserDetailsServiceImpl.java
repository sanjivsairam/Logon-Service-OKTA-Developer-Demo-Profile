package com.demoagro.loginservice.service.impl;

import com.demoagro.loginservice.constants.LoginServiceErrorMessageConstants;
import com.demoagro.loginservice.model.common.UserDetailsImpl;
import com.demoagro.loginservice.model.entity.User;
import com.demoagro.loginservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author rsairam
 *
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	/**
	 *
	 */
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

		User user = userRepository.findByUserName(userName).orElseThrow(() -> new UsernameNotFoundException(
				LoginServiceErrorMessageConstants.USER_NOT_FOUND_WITH_USERNAME + userName));

		return UserDetailsImpl.build(user);
	}

}
