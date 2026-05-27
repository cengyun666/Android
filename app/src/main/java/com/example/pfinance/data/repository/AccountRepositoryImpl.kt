package com.example.pfinance.data.repository

import com.example.pfinance.data.local.dao.AccountDao
import com.example.pfinance.data.local.entity.AccountEntity
import com.example.pfinance.domain.model.Account
import com.example.pfinance.domain.model.AccountType
import com.example.pfinance.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepositoryImpl @Inject constructor(
    private val accountDao: AccountDao
) : AccountRepository {

    override fun getVisibleAccounts(): Flow<List<Account>> =
        accountDao.getVisibleAccounts().map { list -> list.map { it.toDomain() } }

    override fun getAllAccounts(): Flow<List<Account>> =
        accountDao.getAllAccounts().map { list -> list.map { it.toDomain() } }

    override fun getArchivedAccounts(): Flow<List<Account>> =
        accountDao.getArchivedAccounts().map { list -> list.map { it.toDomain() } }

    override suspend fun getAccountById(id: Long): Account? =
        accountDao.getAccountById(id)?.toDomain()

    override suspend fun getTotalAssets(): Double =
        accountDao.getTotalAssets() ?: 0.0

    override suspend fun getTotalLiabilities(): Double =
        -(accountDao.getTotalLiabilities() ?: 0.0) // credit card balance is negative

    override suspend fun insertAccount(account: Account): Long =
        accountDao.insertAccount(account.toEntity())

    override suspend fun updateAccount(account: Account) =
        accountDao.updateAccount(account.toEntity())

    override suspend fun updateBalance(id: Long, delta: Double) =
        accountDao.updateBalance(id, delta)

    override suspend fun archiveAccount(id: Long) = accountDao.archiveAccount(id)
    override suspend fun unarchiveAccount(id: Long) = accountDao.unarchiveAccount(id)
    override suspend fun deleteAccount(account: Account) = accountDao.deleteAccount(account.toEntity())

    private fun AccountEntity.toDomain() = Account(
        id = this.id,
        name = this.name,
        type = AccountType.valueOf(this.type),
        balance = this.balance,
        initialBalance = this.initialBalance,
        currencyCode = this.currencyCode,
        icon = this.icon,
        color = this.color,
        creditLimit = this.creditLimit,
        billDate = this.billDate,
        dueDate = this.dueDate,
        cardNumber = this.cardNumber,
        note = this.note,
        isHidden = this.isHidden,
        isArchived = this.isArchived
    )

    private fun Account.toEntity() = AccountEntity(
        id = this.id,
        name = this.name,
        type = this.type.name,
        balance = this.balance,
        initialBalance = this.initialBalance,
        currencyCode = this.currencyCode,
        icon = this.icon,
        color = this.color,
        creditLimit = this.creditLimit,
        billDate = this.billDate,
        dueDate = this.dueDate,
        cardNumber = this.cardNumber,
        note = this.note,
        isHidden = this.isHidden,
        isArchived = this.isArchived
    )
}
