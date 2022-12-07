package commit.admin.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import commit.etc.dao.AbstractDAO;

@Repository("adminDAO")
public class AdminDAO extends AbstractDAO {

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getOrderList(Map<String, Object> map) {

		return selectList("admin.selectOrderList", map);
	}
	
	
	public void updateOrder(Map<String, Object> map) {
		update("admin.updateOrder", map);
	}
	

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getOrderDetail(Map<String, Object> map) {

		return selectList("admin.selectOrderDatail", map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectMemberList(Map<String, Object> map) {
		return (List<Map<String, Object>>) selectList("admin.selectMemberList", map);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> selectMemberDetail(Map<String, Object> map) {
		return (Map<String, Object>) selectOne("admin.selectMemberDetail", map);
	}

	public void deleteMember(Map<String, Object> map) {
		update("admin.deleteMember", map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selecProList(Map<String, Object> map) {
		return (List<Map<String, Object>>) selectList("admin.selectProList", map);
	}

	public void insertPro(Map<String, Object> map) {
		insert("admin.insertPro", map);

	}

	public void insertProImg(Map<String, Object> map) {
		insert("admin.insertImage", map);

	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> detailPro(Map<String, Object> map) {

		return (Map<String, Object>) selectOne("admin.selectProDetail", map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> detailProImg(Map<String, Object> map) {

		return selectList("admin.selectImageList", map);
	}

	public void updatePro(Map<String, Object> map) {
		update("admin.updatePro", map);

	}

	public void deleteImgList(Map<String, Object> map) {
		update("admin.deleteImageList", map);

	}

	public void updateImgList(Map<String, Object> map) {
		update("admin.updateImageList", map);
	}

	public void deletePro(Map<String, Object> map) {
		update("admin.deletePro", map);
	}

	public void deleteImageList(Map<String, Object> map) {
		update("admin.deleteImageList", map);
	}


	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getProInfo(Map<String, Object> map) {
		
		return selectList("admin.getProInfo", map);
	}


	public void updateStock(Map<String, Object> map) {
		update("admin.updateStock", map);
		
	}


	@SuppressWarnings("unchecked")
	public Map<String, Object> checkCoupon(Map<String, Object> map) {
		return (Map<String, Object>) selectOne("admin.checkCoupon", map);
	}


	public void insertDetail(Map<String, Object> map) {
		insert("admin.insertDetail", map);
		
	}


	public int countMemberRecord(Map<String, Object> map) {
		return (int) selectOne("admin.countMemberRecord",map);
	}


	public int countOrderRecord(Map<String, Object> map) {
		return (int) selectOne("admin.countOrderRecord",map);
	}


	public int countProRecord(Map<String, Object> map) {
		return (int) selectOne("admin.countProRecord",map);
	}


	@SuppressWarnings("unchecked")
	public Map<String, Object> detailStuff(Map<String, Object> map) {
		return (Map<String, Object>) selectOne("admin.detailStuff",map);
	}


	public void updateProDetail(Map<String, Object> map1) {
		update("admin.updateProDetail", map1);
		
	}


	

}
