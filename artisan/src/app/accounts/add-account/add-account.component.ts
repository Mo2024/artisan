import { Component, Output, EventEmitter } from '@angular/core';
import { AccountsService } from '../../services/accounts.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-add-account',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './add-account.component.html',
  styleUrl: './add-account.component.css'
})
export class AddAccountComponent {
  @Output() closeClicked: EventEmitter<void> = new EventEmitter<void>();
  @Output() accountAdded: EventEmitter<any> = new EventEmitter();

  name: string = '';
  balance: string = '';

  constructor(private accountsService: AccountsService) { }

  addAccount(name: string, balance: string) {
    if (!name.trim()) {
      alert('Name and description cannot be empty');
      return; // Exit the function if empty
    }

    this.accountsService.addAccount(name, balance).subscribe({
      next: (response) => {
        console.log('Account added:', response);
        this.accountAdded.emit(response);
        this.closeClicked.emit();
      },
      error: (error) => {
        if (error.error.error) {
          alert(error.error.error)
        } else {
          console.error(error)
          alert('unknown error occured')
        }
      }
    });
  }

  emitCloseClicked(): void {
    this.closeClicked.emit();
  }

}
