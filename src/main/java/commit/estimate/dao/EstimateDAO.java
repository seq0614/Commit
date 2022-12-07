package commit.estimate.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import commit.etc.dao.AbstractDAO;

@Repository("estimateDAO")
public class EstimateDAO extends AbstractDAO {

	// 견적
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectPsList(Map<String, Object> map) {
		return (List<Map<String, Object>>) selectList("estimate.selectPsList", map);
	}

}