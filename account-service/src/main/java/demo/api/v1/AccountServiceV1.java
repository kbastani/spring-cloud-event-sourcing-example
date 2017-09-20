package demo.api.v1;

import demo.account.Account;
import demo.account.AccountRepository;
import demo.user.User;
import demo.user.UserClientV1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AccountServiceV1 {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    UserClientV1 userClientV1;

    public List<Account> getUserAccounts() {
        List<Account> account = null;
        User user = userClientV1.getAuthenticatedUser();
        if (user != null) {
            account = accountRepository.findAccountsByUserId(user.getUsername());
        }

        // Mask credit card numbers
        if (account != null) {
            account.forEach(acct -> acct.getCreditCards()
                    .forEach(card ->
                            card.setNumber(card.getNumber()
                                    .replaceAll("([\\d]{4})(?!$)", "****-"))));
        }

        return account;
    }
}
