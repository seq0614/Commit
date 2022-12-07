package commit.main.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import commit.etc.dao.AbstractDAO;

@Repository("mainDAO")
public class MainDAO extends AbstractDAO {

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getGamingPC() {
		
		return selectList("main.selectGamePC");
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getOfficePC() {
		// TODO Auto-generated method stub
		return selectList("main.selectOfficePC");
	}

}
