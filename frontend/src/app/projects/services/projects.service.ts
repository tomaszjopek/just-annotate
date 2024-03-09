import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { Project } from "../projects.reducer";

@Injectable({
  providedIn: 'root'
})
export class ProjectsService {

  constructor(private httpClient: HttpClient) { }

  fetchProjects(): Observable<Project[]> {
    return this.httpClient.get<Project[]>('http://localhost:3000/projects')
  }

}
