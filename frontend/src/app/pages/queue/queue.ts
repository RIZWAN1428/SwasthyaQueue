import { Component, OnInit, signal } from '@angular/core';
import { QueueToken, QueueTokenResponse } from '../../services/queue-token';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-queue',
  imports: [],
  templateUrl: './queue.html',
  styleUrl: './queue.scss',
})
export class Queue implements OnInit {

  tokens = signal<QueueTokenResponse[]>([]);
  //!! — converts the result to a plain true/false 
  // (double negation: ! flips it to boolean-opposite, second ! flips it back correctly typed as boolean)
  isLoggedIn = !!localStorage.getItem('token');
  
//Angular's way to read URL parameters (here, the :id from /queue/:id).
  constructor(private route: ActivatedRoute, private queueTokenService: QueueToken) {}

  ngOnInit(){
    // /this.route.snapshot.paramMap.get('id') — pulls the actual id value out of the current URL, as a string
    //Number(...) converts it since your service expects a number
    const departmentId  = Number(this.route.snapshot.paramMap.get('id'));

    this.queueTokenService.getAllTokensForDepartment(departmentId).subscribe({
      next: (data) => this.tokens.set(data),
      error: (err) => console.log('Failed to load queue', err),
    });
  }

  //Complete Token
  completeToken(tokenId: number){
    this.queueTokenService.completeToken(tokenId).subscribe({
      next: () =>{
        //Refresh the list after completed the token
        //Simple approach: re-fetches from server rather than removing it manually from an array.
        this.ngOnInit();
      },
      error: (err) => console.log('Failed to complete token', err),
    });
  }

}
