
import java.util.HashMap;
import java.util.Map;

import com.kaikeba.service.SqlSession;
import com.kaikeba.serviceImpl.DeptMapper;
import com.kaikeba.util.SqlSessionFactory;

public class TestMain {

	public static void main(String[] args) throws Exception {
	   
		Map StatementMapper = new HashMap();
	    StatementMapper.put("dept.save", "insert into dept values(50,'TEST','BEIJING',1)");
		
		SqlSession dao =   SqlSessionFactory.Builder(DeptMapper.class);	    	    
		dao.save((String)StatementMapper.get("dept.save"));
	}

}
