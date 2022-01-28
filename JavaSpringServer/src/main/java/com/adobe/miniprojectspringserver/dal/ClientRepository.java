

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface ClientRepository  extends CrudRepository<Client, Integer>{

//  @Override
//  <S extends User> S save(S s);
//
//  @Override
//  <S extends User> Iterable<S> saveAll(Iterable<S> iterable);
//
//  @Override
//  Optional<User> findById(Integer integer);
//
//  @Override
//  boolean existsById(Integer integer);
//
//  @Override
//  public List<User> findAll();
//
//  @Override
//  Iterable<User> findAllById(Iterable<Integer> iterable);
//
//  @Override
//  long count();
//
//  @Override
//  void deleteById(Integer integer);
//
//  @Override
//  void delete(User User);
//
//  @Override
//  void deleteAll(Iterable<? extends User> iterable);
//
//  @Override
//  void deleteAll();
	
//	@Query("select p from Product as p where p.qoh=:qty")
//	public List<Product> complexQuery(@Param("qty")int qty);
//	@Query("update users set email='1234@abc' where id=1")
//	public void editById();
}
