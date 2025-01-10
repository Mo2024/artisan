import { Component } from '@angular/core';
import { AccountsService } from '../services/accounts.service';
import { CommonModule } from '@angular/common';
import { AddAccountComponent } from './add-account/add-account.component';
import { AddBalanceComponent } from './add-balance/add-balance.component';

@Component({
  selector: 'app-accounts',
  standalone: true,
  imports: [AddAccountComponent, CommonModule, AddBalanceComponent],
  templateUrl: './accounts.component.html',
  styleUrl: './accounts.component.css'
})
export class AccountsComponent {
  showAddAccount: boolean = false;
  showEditAccount: boolean = false;
  showAddBalance: boolean = false;
  accounts!: any[];
  selectedAccount: any;

  constructor(private accountsService: AccountsService) { }

  ngOnInit(): void {
    this.getAccounts();
  }

  toggleAddAccount(): void {
    this.showAddAccount = !this.showAddAccount;
  }

  toggleAddBalance(): void {
    this.showAddBalance = !this.showAddBalance;
  }

  toggleEditAccount(): void {
    this.showEditAccount = !this.showEditAccount;
  }

  getAccounts(): void {
    this.accountsService.getAccounts().subscribe({
      next: (response) => {
        this.accounts = response;
      },
      error: (error) => {
        if (error.error.error) {
          alert(error.error.error)
        } else {
          alert('unknown error occured')
        }
      }
    });
  }

  handleAccountAdded(response: any) {
    this.accounts = response;
  }

  handleBalanceAdded(response: any) {
    this.accounts = response;
  }


  editAccount(account: any) {
    // Pass the selected account to the edit account component
    this.selectedAccount = account;
    // Toggle the visibility of the edit account component
    this.toggleEditAccount();
  }

  addBalance() {

  }


}
