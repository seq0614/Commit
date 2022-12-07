package commit.notice.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface NoticeService {

	List<Map<String, Object>> selectNoticeList(Map<String, Object> map) throws Exception;

	void insertNotice(Map<String, Object> map, HttpServletRequest request) throws Exception;

	Map<String, Object> selectNoticeDetail(Map<String, Object> map, HttpServletRequest request) throws Exception;

	void updateNotice(Map<String, Object> map, HttpServletRequest request) throws Exception;

	void deleteNotice(Map<String, Object> map) throws Exception;

	void selectFileInfo(Map<String, Object> map, HttpServletResponse response, HttpServletRequest request) throws Exception;

	//Map<String, Object> selectNoticeUpdate(Map<String, Object> map) throws Exception;

	void insertComment(Map<String, Object> map, HttpServletRequest request) throws Exception;

	void deleteComment(Map<String, Object> map)throws Exception;

	List<Map<String, Object>> selectCommentData(Map<String, Object> map) throws Exception;



}
