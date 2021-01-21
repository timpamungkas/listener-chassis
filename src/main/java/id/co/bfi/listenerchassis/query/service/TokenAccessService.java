package id.co.bfi.listenerchassis.query.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import id.co.bfi.listenerchassis.query.action.TokenAccessAction;

@Service
public class TokenAccessService {

	@Autowired
	private TokenAccessAction action;

	public String getValidTokenAccess(String username, String password) {
		var existingTokenAccess = action.getExistingTokenAccess(username);

		if (StringUtils.isNotBlank(existingTokenAccess)) {
			return existingTokenAccess;
		} else {
			return action.getNewTokenAccess(username, password);
		}
	}

	public void deleteExpiredTokens() {
		action.deleteExpiredTokens();
	}

}
