package com.Wallet;

import com.User.User;
import com.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletDAO {

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;

    public List<Wallet> findAllUserWallets(User user){
        return walletRepository.findWalletByUser(user);
    }

    public List<Wallet> findAllUserSavingsWallets(User user){
        boolean savings = true;
        return walletRepository.findWalletByUserAndSavings(user, savings);
    }

    public Wallet createNewWallet(String nameWallet, float balance, float financialGoal, String comment, boolean savings, String emailUser){
        Wallet wallet = new Wallet.Builder()
                .nameWallet(nameWallet)
                .balance(balance)
                .financialGoal(financialGoal)
                .comment(comment)
                .savings(savings)
                .user(userRepository.findUserByEmail(emailUser).get())
                .build();
        return walletRepository.save(wallet);
    }
    public boolean checkIfUserHasWalletWithTheGivenName(List<Wallet> userWallets, String newWalletName){
        return userWallets.stream().anyMatch(o -> o.getNameWallet().equals(newWalletName));
    }

    public Wallet findWalletByIdWallet(int idWallet){
        return walletRepository.findWalletByIdWallet(idWallet);
    }
}
