/*
 * Copyright (C) 2012 - 2012 NHN Corporation
 * All rights reserved.
 *
 * This file is part of The nGrinder software distribution. Refer to
 * the file LICENSE which is part of The nGrinder distribution for
 * licensing details. The nGrinder distribution is available on the
 * Internet at http://nhnopensource.org/ngrinder
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT HOLDERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.ngrinder.user.service;

import static org.ngrinder.common.util.Preconditions.checkNotEmpty;
import static org.ngrinder.common.util.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ngrinder.model.Role;
import org.ngrinder.model.User;
import org.ngrinder.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class UserService.
 * 
 * @author Yubin Mao & AlexQin
 */
@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	/**
	 * get user by user id.
	 * 
	 * @param userId
	 *            user id
	 * @return user
	 */
	public User getUserById(String userId) {
		return userRepository.findOneByUserId(userId);
	}

	/**
	 * get user by userName.
	 * 
	 * @param userName
	 * @return user
	 */
	public User getUserByUserName(String userName) {
		return userRepository.findOneByUserName(userName);
	}

	/**
	 * get all users in group admin, general, and super user.
	 * 
	 * @return user map.
	 */
	public Map<Role, List<User>> getAllUserInGroup() {
		List<User> users = userRepository.findAll();
		Map<Role, List<User>> rtnMap = new HashMap<Role, List<User>>();

		for (User user : users) {
			Role currRole = user.getRole();
			List<User> userList = new ArrayList<User>();

			if (!rtnMap.containsKey(currRole)) {
				userList = new ArrayList<User>();
				userList.add(user);
				rtnMap.put(currRole, userList);
			} else {
				rtnMap.get(currRole).add(user);
			}
		}

		return rtnMap;
	}

	/**
	 * create user.
	 * 
	 * @param user
	 *            , include id, userID, fullName, role, password.
	 * 
	 * @return result
	 */
	@Transactional
	public User saveUser(User user) {
		return userRepository.save(user);
	}

	/**
	 * Add normal user
	 * 
	 * @param user
	 */
	public void saveUser(User user, Role role) {
		user.setRole(role);
		userRepository.save(user);
	}

	/**
	 * modify user information.
	 * 
	 * @param user
	 * @return result.
	 */
	public String modifyUser(User user) {
		checkNotNull(user, "user should be not null, when modifying user");
		checkNotEmpty(user.getUserId(), "user id should be provided when modifying user");
		User targetUser = userRepository.findOneByUserId(user.getUserId());
		targetUser.merge(user);
		userRepository.save(targetUser);
		return user.getUserId();
	}

	/**
	 * Delete user.
	 * 
	 * @param userIds
	 *            the user id string
	 * @return true, if successful
	 */
	public void deleteUsers(List<String> userIds) {
		// TODO: delete user, how about the projects created by user
		for (String userId : userIds) {
			userRepository.deleteByUserId(userId);
		}
	}

	/**
	 * get user list by role.
	 * 
	 * @param paramMap
	 * @return user list
	 * @throws Exception
	 */
	public List<User> getUserListByRole(Role role) {
		return userRepository.findAllByRole(role);
	}

}