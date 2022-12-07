package commit.admin.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AdminService {

	List<Map<String, Object>> getOrderList(Map<String, Object> map) throws Exception;
	
	void updateOrder(Map<String, Object> map) throws Exception;

	List<Map<String, Object>> getOrderDetail(Map<String, Object> map) throws Exception;

	List<Map<String, Object>> selectMemberList(Map<String, Object> map) throws Exception;

	Map<String, Object> selectMemberDetail(Map<String, Object> map) throws Exception;

	void deleteMember(Map<String, Object> map) throws Exception;

	List<Map<String, Object>> selectProList(Map<String, Object> map) throws Exception;

	void insertPro(Map<String, Object> map, HttpServletRequest request) throws Exception;

	Map<String, Object> detailPro(Map<String, Object> map) throws Exception;

	void updatePro(Map<String, Object> map, HttpServletRequest request) throws Exception;

	void deletePro(Map<String, Object> map, HttpServletResponse response) throws Exception;

}
