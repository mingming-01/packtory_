package kr.co.itwillbs.de.common.mapper;

import org.apache.ibatis.annotations.Mapper;

import kr.co.itwillbs.de.common.vo.LoginVO;

@Mapper
public interface LoginMapper {

	
//	LoginVO getMemberByUserName(LoginVO loginVO);

	/**
	 * 
	 * @param userName
	 * @return
	 */
	LoginVO getMemberByUserName(String userName);

}
