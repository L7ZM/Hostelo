import { Component } from '@angular/core';

@Component({
  selector: 'app-about',
  templateUrl: './about.component.html',
  styleUrls: ['./about.component.scss']
})
export class AboutComponent {
  facilities = [
    {
      image: 'https://images.pexels.com/photos/271624/pexels-photo-271624.jpeg',
      title: 'Elegant Rooms',
      description: 'Spacious and beautifully designed rooms with modern comforts.'
    },
    {
      image: 'https://images.pexels.com/photos/262047/pexels-photo-262047.jpeg',
      title: 'Restaurant & Lounge',
      description: 'A fine dining experience and a chic lounge for relaxation.'
    },
    {
      image: 'https://images.pexels.com/photos/1181396/pexels-photo-1181396.jpeg',
      title: 'Event Spaces',
      description: 'Versatile spaces for meetings, conferences, and special events.'
    },
    {
      image: 'https://images.pexels.com/photos/3768916/pexels-photo-3768916.jpeg',
      title: 'Wellness Center',
      description: 'Stay fit and rejuvenate with our gym, spa, and wellness services.'
    },
    {
      image: 'assets/roofTopar.jpg',
      title: 'Rooftop Bar',
      description: 'Enjoy breathtaking views while sipping cocktails at our stylish rooftop bar, perfect for relaxation and socializing.'
    },
    {
      image: 'assets/kidsPlay.bmp',
      title: "Kids' Play Area",
      description: 'A safe and exciting space for children to play, learn, and interact with other kids in a fun environment.'
    }
  ];
}
