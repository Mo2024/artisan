import { Component } from '@angular/core';
import { AccountsService } from '../services/accounts.service';
import { CommonModule } from '@angular/common';
import { AddAccountComponent } from './add-account/add-account.component';

@Component({
  selector: 'app-accounts',
  standalone: true,
  imports: [AddAccountComponent, CommonModule],
  templateUrl: './accounts.component.html',
  styleUrl: './accounts.component.css'
})
export class AccountsComponent {
  showAddAccount: boolean = false;
  showEditAccount: boolean = false;
  accounts!: any[];
  selectedAccount: any;

  constructor(private accountsService: AccountsService) { }

  ngOnInit(): void {
    this.getAccounts();
  }

  toggleAddAccount(): void {
    this.showAddAccount = !this.showAddAccount;
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
        console.error('Error fetching accounts:', error);
      }
    });
  }

  handleAccountAdded(response: any) {
    this.accounts = response;
  }


  editAccount(account: any) {
    // Pass the selected account to the edit account component
    this.selectedAccount = account;
    // Toggle the visibility of the edit account component
    this.toggleEditAccount();
  }


}
