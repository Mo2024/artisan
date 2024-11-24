import { Component } from '@angular/core';
import { AddSiteComponent } from './add-site/add-site.component';
import { CommonModule } from '@angular/common';
import { SitesService } from '../services/sites.service';
import { EditSiteComponent } from './edit-site/edit-site.component';

@Component({
  selector: 'app-sites',
  standalone: true,
  imports: [AddSiteComponent, EditSiteComponent, CommonModule],
  templateUrl: './sites.component.html',
  styleUrl: './sites.component.css'
})
export class SitesComponent {
  showAddSite: boolean = false;
  showEditSite: boolean = false;
  sites!: any[];
  selectedSite: any;

  constructor(private sitesService: SitesService) { }

  ngOnInit(): void {
    this.getSites();
  }

  toggleAddSite(): void {
    this.showAddSite = !this.showAddSite;
  }

  toggleEditSite(): void {
    this.showEditSite = !this.showEditSite;
  }

  getSites(): void {
    this.sitesService.getSites().subscribe({
      next: (response) => {
        this.sites = response;
      },
      error: (error) => {
        console.error('Error fetching sites:', error);
      }
    });
  }

  handleSiteAdded(response: any) {
    this.sites = response;
  }

  handleSiteEdited(response: any) {
    this.sites = response;
  }
  editSite(site: any) {
    // Pass the selected site to the edit site component
    this.selectedSite = site;
    // Toggle the visibility of the edit site component
    this.toggleEditSite();
  }
  deleteSite(site: any) {
    const { id } = site;
    this.sitesService.deleteSite(id).subscribe({
      next: (response) => {
        this.sites = response;
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
}
