import { HttpClient } from '@angular/common/http';
//Angular's built-in tool for making HTTP requests
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class Auth {

    //Storing the base url once so don't need to repeat everywhere.
    private apiUrl = 'http://localhost:8080/api/staff';

    //Angular's own DI(same concept as @Autowired in spring , angular hands us a working httpClient instance
    //automatically)
    constructor(private http:HttpClient){}

    login(userName: string, password: string){
      //makes a post request and tells TS expect the response to have a token field of type string.(like DTO in backend)
      return this.http.post<{ token: string }>(`${this.apiUrl}/login`, { userName, password });
    }


}
