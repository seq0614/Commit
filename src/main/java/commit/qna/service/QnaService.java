package commit.qna.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface QnaService {

	//void insertAnswer(Map<String, Object> map)throws Exception;

	//void insertQuestion(Map<String, Object> map, HttpServletRequest request)throws Exception;

	Map<String,Object> detailQna(Map<String, Object> map)throws Exception;

	void updateQna(Map<String, Object> map)throws Exception;

	void deleteQna(Map<String, Object> map, HttpServletRequest request)throws Exception;

	void insertQna(Map<String, Object> map, HttpServletRequest request);



}
