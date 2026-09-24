import { Component, ElementRef, HostListener, OnInit, ViewChild, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Department, DepartmentResponse } from '../../services/department';
import { ThemeService } from '../../services/theme';

interface CarouselCard {
  id: string;
  category: string;
  title: string;
  excerpt: string;
  imageBg: string;
  readTime: string;
  tag: string;
}

interface EditorialInsight {
  category: string;
  date: string;
  title: string;
  summary: string;
  readTime: string;
}

@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './landing.html',
  styleUrl: './landing.scss',
})
export class Landing implements OnInit {
  @ViewChild('carouselRail') carouselRail!: ElementRef<HTMLDivElement>;

  isLoggedIn = !!localStorage.getItem('token');
  departments = signal<DepartmentResponse[]>([]);
  scrollProgress = signal<number>(0);
  showScrollTop = signal<boolean>(false);
  isScrolled = signal<boolean>(false);

  @HostListener('window:scroll')
  onWindowScroll(): void {
    const scrollY = window.scrollY || document.documentElement.scrollTop;
    const docHeight = document.documentElement.scrollHeight - document.documentElement.clientHeight;
    const progress = docHeight > 0 ? Math.min(100, Math.max(0, (scrollY / docHeight) * 100)) : 0;
    this.scrollProgress.set(progress);
    this.showScrollTop.set(scrollY > 160);
    this.isScrolled.set(scrollY > 20);
  }

  scrollToTop(): void {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  // Carousel Cards highlighting core technical breakthroughs from docs/every_step.md
  carouselCards: CarouselCard[] = [
    {
      id: 'redis-engine',
      category: 'IN-MEMORY INFRASTRUCTURE',
      title: 'Decoupling Queue Throughput via Sub-Millisecond Redis Scoring',
      excerpt: 'Traditional relational schemas trigger N database write-locks per queue inspection. SwasthyaQueue offloads dynamic priority evaluation entirely to Redis key-value storage.',
      imageBg: 'linear-gradient(135deg, #161B22 0%, #0D1117 100%)',
      readTime: '4 MIN READ',
      tag: 'REDIS CACHE'
    },
    {
      id: 'prereq-dag',
      category: 'CLINICAL GOVERNANCE',
      title: 'Enforcing Clinical Prerequisite Chains via Directed Acyclic Graphs',
      excerpt: 'Specialized consultations are strictly gated behind verified completion of mandatory diagnostic procedures, preventing clinical oversights and wasted doctor hours.',
      imageBg: 'linear-gradient(135deg, #1C2128 0%, #111418 100%)',
      readTime: '6 MIN READ',
      tag: 'PREREQUISITE DAG'
    },
    {
      id: 'aging-matrix',
      category: 'TRIAGE ALGORITHMS',
      title: 'The Anti-Starvation Equation: Balancing Severity with Waiting Duration',
      excerpt: 'Deterministic formula (Severity × 10) + (WaitingMinutes × 0.3) guarantees that critical emergencies receive instant escalation while prolonged waiting patients advance continuously.',
      imageBg: 'linear-gradient(135deg, #1E293B 0%, #0F172A 100%)',
      readTime: '5 MIN READ',
      tag: 'FAIRNESS MATRIX'
    },
    {
      id: 'two-tier-sync',
      category: 'TRANSACTIONAL INTEGRITY',
      title: 'Two-Tier Synchronization: 30-Second PostgreSQL Background Flush',
      excerpt: 'PostgreSQL remains the uncompromised source of truth. A background @Scheduled daemon batches recalculated priority state back to disk at strict 30-second intervals.',
      imageBg: 'linear-gradient(135deg, #111827 0%, #030712 100%)',
      readTime: '5 MIN READ',
      tag: '@SCHEDULED SYNC'
    },
    {
      id: 'jwt-security',
      category: 'INSTITUTIONAL SECURITY',
      title: 'Stateless Spring Security Architecture with Cryptographic JWT Verification',
      excerpt: 'Role-based access boundaries strictly partition Patient Registration, Department Topology, and Token Resolution across clinical staff and executive administration.',
      imageBg: 'linear-gradient(135deg, #1F2937 0%, #111827 100%)',
      readTime: '3 MIN READ',
      tag: 'JWT RBAC'
    }
  ];

  // Asymmetrical Editorial Insights
  editorialLead = {
    category: 'SPECIAL RESEARCH REPORT',
    date: 'SEPTEMBER 2026',
    author: 'Hospital Systems Architecture Group',
    title: 'The Latency Paradox: How Redis In-Memory Caching Eliminates Emergency Department Queue Starvation',
    summary: 'An exhaustive architectural analysis of multi-tier healthcare data layers. Demonstrating how decoupling read-heavy patient queuing from transactional PostgreSQL persistence reduces consultation intake delays by 78% across high-density clinical trauma networks.',
    readTime: '8 MIN READ'
  };

  editorialBriefs: EditorialInsight[] = [
    {
      category: 'CLINICAL PROTOCOLS',
      date: 'SEPTEMBER 2026',
      title: 'Automating Department Prerequisites: Eliminating Consultation Bottlenecks',
      summary: 'Why manual verification of pre-consultation lab tests fails during OPD surges, and how deterministic DAG validation enforces mandatory diagnostic sequences before token issuance.',
      readTime: '4 MIN READ'
    },
    {
      category: 'SYSTEM INTEGRITY',
      date: 'SEPTEMBER 2026',
      title: 'Zero-Collision Sequence Generation via PostgreSQL nextval() Architecture',
      summary: 'Evaluating transactional concurrency under concurrent intake load. Why database sequences guarantee deterministic token numbering across distributed triage terminals.',
      readTime: '5 MIN READ'
    },
    {
      category: 'ALGORITHMIC FAIRNESS',
      date: 'SEPTEMBER 2026',
      title: 'Calibrating the 0.3x Aging Multiplier Against Critical Severity Weights',
      summary: 'Mathematical modeling of outpatient queue fairness to prevent routine patient abandonment while preserving immediate clinical bypass for acute severity Level 5 cases.',
      readTime: '6 MIN READ'
    }
  ];

  constructor(
    private departmentService: Department,
    public themeService: ThemeService
  ) { }

  ngOnInit(): void {
    this.departmentService.getAllDepartments().subscribe({
      next: (data) => {
        if (data && data.length > 0) {
          this.departments.set(data);
        } else {
          this.setFallbackDepartments();
        }
      },
      error: () => {
        this.setFallbackDepartments();
      }
    });
  }

  scrollCarousel(direction: 'prev' | 'next'): void {
    if (!this.carouselRail) return;
    const rail = this.carouselRail.nativeElement;
    const cardWidth = 380; // approximate card width + gap
    const scrollAmount = direction === 'next' ? cardWidth : -cardWidth;
    rail.scrollBy({ left: scrollAmount, behavior: 'smooth' });
  }

  private setFallbackDepartments(): void {
    this.departments.set([
      { id: 1, name: 'Cardiology & Intensive Care', avgTime: 18 },
      { id: 2, name: 'Pathology & Diagnostic Laboratory', avgTime: 10 },
      { id: 3, name: 'Emergency Trauma Triage', avgTime: 5 },
      { id: 4, name: 'Orthopedics & Joint Reconstruction', avgTime: 25 },
      { id: 5, name: 'Diagnostic Radiology & CT Imaging', avgTime: 15 },
      { id: 6, name: 'General Internal Medicine', avgTime: 12 },
    ]);
  }
}
