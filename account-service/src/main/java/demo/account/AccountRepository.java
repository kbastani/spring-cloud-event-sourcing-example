package demo.account;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * A {@link PagingAndSortingRepository} for the {@link Account} domain class that provides
 * basic data management capabilities that include paging and sorting results.
 *
 * @author Kenny Bastani
 * @author Josh Long
 */
public interface AccountRepository extends PagingAndSortingRepository<Account, Long> {
    List<Account> findAccountsByUserId(@Param("userId") String userId);
}
