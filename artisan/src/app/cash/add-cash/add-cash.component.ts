import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CashService } from '../../services/cash.service';
import { SitesService } from '../../services/sites.service';

@Component({
  selector: 'app-add-cash',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './add-cash.component.html',
  styleUrl: './add-cash.component.css'
})
export class AddCashComponent {
  @Output() closeClicked: EventEmitter<void> = new EventEmitter<void>();
  @Output() cashAdded: EventEmitter<any> = new EventEmitter();
  @Input() sites!: Array<any>;
  @Input() suppliers!: Array<any>;
  @Input() accounts!: Array<any>;


  date: string = '';
  paid_by: string = '';
  payment_method: string = '';
  cost: string = '';
  description: string = '';
  site_id!: string;
  account_id = null
  selectedAccountIndex!: number

  constructor(private cashService: CashService, private sitesService: SitesService) { }

  addCash() {
    if (!this.date.trim() || !this.paid_by.trim() || !this.payment_method || !this.site_id || !this.description.trim()) {
      alert('All fields must be filled out');
      return;
    }

    if (isNaN(parseFloat(this.cost)) || parseFloat(this.cost) < 1) {
      alert('Cost must be a valid number greater than one');
      return;
    }


    if (this.payment_method == 'bank account' && this.selectedAccountIndex) {
      this.account_id = this.accounts[this.selectedAccountIndex].id
      if (this.cost > this.accounts[this.selectedAccountIndex].balance) {
        alert('No enough balance!');
        return
      }

    } else {
      this.account_id = null
    }

    this.cashService.addCash(this.date, this.paid_by, this.payment_method, this.site_id, this.cost, this.description, this.account_id).subscribe({
      next: (response) => {
        console.log('Cash added:', response);
        this.cashAdded.emit(response);
        this.closeClicked.emit();
      },
      error: (error) => {
        console.error('Error adding site:', error);
      }
    });
  }

  emitCloseClicked(): void {
    this.closeClicked.emit();
  }
}
