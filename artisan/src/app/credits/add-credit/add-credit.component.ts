import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CreditsService } from '../../services/credits.service';
import { SitesService } from '../../services/sites.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-add-credit',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './add-credit.component.html',
  styleUrl: './add-credit.component.css'
})
export class AddCreditComponent {
  @Output() closeClicked: EventEmitter<void> = new EventEmitter<void>();
  @Output() creditAdded: EventEmitter<any> = new EventEmitter();
  @Input() sites!: Array<any>;
  @Input() suppliers!: Array<any>;


  date: string = '';
  invoice_no: string = '';
  supplier_id!: number;
  cost: string = '';
  description: string = '';
  site_id!: number;

  constructor(private creditsService: CreditsService, private sitesService: SitesService) { }

  addCredit() {
    if (!this.date.trim() || !this.invoice_no.trim() || !this.supplier_id || !this.site_id || !this.description.trim()) {
      alert('All fields must be filled out');
      return;
    }

    if (isNaN(parseFloat(this.cost)) || parseFloat(this.cost) < 1) {
      alert('Cost must be a valid number greater than one');
      return;
    }

    this.creditsService.addCredit(this.date, this.invoice_no, this.supplier_id, this.cost, this.description, this.site_id).subscribe({
      next: (response) => {
        console.log('Site added:', response);
        this.creditAdded.emit(response);
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
