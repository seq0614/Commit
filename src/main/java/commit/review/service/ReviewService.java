package commit.review.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface ReviewService {

	List<Map<String, Object>> selectMyReview(Map<String, Object> map,HttpServletRequest request)throws Exception;

	void insertReview(Map<String, Object> map, HttpServletRequest request)throws Exception;

	void deleteReview(Map<String, Object> map)throws Exception;

	String checkValid(Map<String, Object> map, HttpServletRequest request) throws Exception;

}
