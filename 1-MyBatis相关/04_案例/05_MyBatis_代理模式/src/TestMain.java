import com.kaikeba.service.BaseService;
import com.kaikeba.serviceImpl.Dog;
import com.kaikeba.serviceImpl.Person;
import com.kaikeba.util.ProxyFactory;

public class TestMain {

	public static void main(String[] args) throws Exception {
	   
		//mike.eat();
		//Person mike = new Person();
        BaseService dog= ProxyFactory.Builder(Dog.class);
        dog.eat();//专门负责监控mike的代理对象
        
        BaseService mike= ProxyFactory.Builder(Person.class);
        mike.eat();
       
	}

}
