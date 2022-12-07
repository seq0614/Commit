package commit.member.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import commit.etc.dao.AbstractDAO;

@Repository("memberDAO")
public class MemberDAO extends AbstractDAO {

	@SuppressWarnings("unchecked")
	public Map<String, Object> checkMember(Map<String, Object> map) throws Exception {
		return (Map<String, Object>) selectOne("member.selectMemberDetail", map);
	}

	public int confirmId(Map<String, Object> map) {
		return (Integer) selectOne("member.selectConfirmId", map);
	}

	public int confirmEmail(Map<String, Object> map) {
		return (Integer) selectOne("member.selectConfirmEmail", map);
	}

	public void join(Map<String, Object> map) {
		insert("member.insertMember", map);

	}

	public int checkID(Map<String, Object> map) {
		return (Integer) selectOne("member.checkID", map);
	}

	public String findID(Map<String, Object> map) {
		return (String) selectOne("member.findID", map);
	}

	public int checkPW(Map<String, Object> map) {
		return (Integer) selectOne("member.checkPW", map);
	}

	public void updateTempPW(Map<String, Object> map) {
		update("member.updateTempPW", map);
	}

}
