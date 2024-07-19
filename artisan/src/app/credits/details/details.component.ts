import { CommonModule, DatePipe } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SitesService } from '../../services/sites.service';
import { CreditsService } from '../../services/credits.service';
import { format } from 'date-fns';

@Component({
  selector: 'app-details',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './details.component.html',
  styleUrl: './details.component.css'
})
export class DetailsComponent {
  isEditing: boolean = false;
  @Output() closeClicked: EventEmitter<void> = new EventEmitter<void>();
  @Output() creditEdited: EventEmitter<any> = new EventEmitter();
  @Input() credit: any;
  @Input() sites: any;
  @Input() suppliers!: Array<any>;

  date: string = '';
  invoice_no: string = '';
  supplier_id!: number;
  cost: string = '';
  description: string = '';
  site_id!: number;
  date_recorded: string = '';
  date_edited: string = '';

  constructor(private creditsService: CreditsService, private sitesService: SitesService) { }


  ngOnInit(): void {
    this.date = this.credit.date
    this.invoice_no = this.credit.invoice_no
    this.supplier_id = this.credit.supplier_id
    this.cost = this.credit.cost
    this.description = this.credit.description
    this.site_id = this.credit.site_id
    this.date_recorded = this.credit.date_recorded
    this.date_edited = this.credit.date_edited

  }


  emitCloseClicked(): void {
    this.closeClicked.emit();
  }

  editCredit() {
    if (!this.date.trim() || !this.invoice_no.trim() || !this.supplier_id || !this.site_id || !this.description.trim()) {
      alert('All fields must be filled out');
      return;
    }

    if (isNaN(parseFloat(this.cost)) || parseFloat(this.cost) <= 0) {
      alert('Cost must be a valid number greater than one');
      return;
    }

    this.creditsService.editCredit(this.credit.id, this.date, this.invoice_no, this.supplier_id, this.cost, this.description, this.site_id).subscribe({
      next: (response) => {
        console.log('Site added:', response);
        const currentDate = new Date();
        const formattedDate = format(currentDate, 'dd-MM-yyyy');
        this.date_edited = formattedDate;
        this.credit.date_edited = formattedDate;
        this.creditEdited.emit(response);
        this.toggleEditCredit()
      },
      error: (error) => {
        console.error('Error adding site:', error);
      }
    });




  }

  toggleEditCredit() {
    this.isEditing = !this.isEditing
  }

  deleteCredit(credit: any) {
    const { id } = credit;
    this.creditsService.deleteCredit(id).subscribe({
      next: (response) => {
        this.creditEdited.emit(response);
        this.closeClicked.emit();
      },
      error: (error) => {
        console.error('Error deleting supplier:', error);
      }
    });
  }

}
