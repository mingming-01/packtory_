package kr.co.itwillbs.de.approval.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.itwillbs.de.approval.dto.NoticeDTO;
import kr.co.itwillbs.de.approval.dto.NoticeSearchDTO;
import kr.co.itwillbs.de.common.vo.FileVO;
import kr.co.itwillbs.de.sample.dto.FileDTO;

@Mapper
public interface NoticeMapper {
	List<NoticeDTO> getNoticeList(String major_code);
	
	NoticeDTO getNotice(String idx);
	
	int registerNotice(NoticeDTO noticeVO);
	
	List<NoticeDTO> getNoticeSearchList(@Param("major_code") String major_code ,
										@Param("noticeSearchDTO") NoticeSearchDTO noticeSearchDTO);
	
	int modifyNotice(NoticeDTO noticeDTO);
	
	int removeNotice(NoticeDTO noticeDTO);

	int removeFile(FileVO fileVO);
	
	
}
