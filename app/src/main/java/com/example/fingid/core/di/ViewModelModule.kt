package com.example.fingid.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fingid.presentation.feature.balance.viewmodel.BalanceScreenViewModel
import com.example.fingid.presentation.feature.balance_update.viewmodel.BalanceUpdateScreenViewModel
import com.example.fingid.presentation.feature.categories.viewmodel.CategoriesScreenViewModel
import com.example.fingid.presentation.feature.expenses.viewmodel.ExpensesScreenViewModel
import com.example.fingid.presentation.feature.history.viewmodel.HistoryScreenViewModel
import com.example.fingid.presentation.feature.incomes.viewmodel.IncomeScreenViewModel
import com.example.fingid.presentation.feature.main.viewmodel.MainScreenViewModel
import com.example.fingid.presentation.feature.settings.viewmodel.SettingsScreenViewModel
import com.example.fingid.presentation.feature.transaction_creation.viewmodel.TransactionCreationViewModel
import com.example.fingid.presentation.feature.transaction_update.viewmodel.TransactionUpdateViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass


@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(BalanceScreenViewModel::class)
    fun bindBalanceViewModel(vm: BalanceScreenViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BalanceUpdateScreenViewModel::class)
    fun bindBalanceUpdateViewModel(vm: BalanceUpdateScreenViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CategoriesScreenViewModel::class)
    fun bindCategoriesViewModel(vm: CategoriesScreenViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ExpensesScreenViewModel::class)
    fun bindExpensesViewModel(vm: ExpensesScreenViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HistoryScreenViewModel::class)
    fun bindHistoryViewModel(vm: HistoryScreenViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(IncomeScreenViewModel::class)
    fun bindIncomesViewModel(vm: IncomeScreenViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainScreenViewModel::class)
    fun bindMainViewModel(vm: MainScreenViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsScreenViewModel::class)
    fun bindSettingsViewModel(vm: SettingsScreenViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TransactionCreationViewModel::class)
    fun bindTransactionCreationViewModel(vm: TransactionCreationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TransactionUpdateViewModel::class)
    fun bindTransactionUpdateViewModel(vm: TransactionUpdateViewModel): ViewModel

    @Binds
    fun bindViewModelFactory(factory: ViewModelProviderFactory): ViewModelProvider.Factory
}

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)