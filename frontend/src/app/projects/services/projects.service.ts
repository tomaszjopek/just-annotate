import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { Project } from "../projects.reducer";
import { ConfigurationService } from "../../core/services/configuration.service";

@Injectable({
  providedIn: 'root'
})
export class ProjectsService {

  constructor(private httpClient: HttpClient, private configService: ConfigurationService) {
  }

  fetchProjects(): Observable<Project[]> {
    return this.httpClient.get<Project[]>(`${this.configService.getApiUrl()}/projects`)
  }

}
