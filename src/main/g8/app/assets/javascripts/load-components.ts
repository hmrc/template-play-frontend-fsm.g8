import { Component } from './components/component';

import './legacy/research-banner';

export default function loadComponents(): void {
  // load components here
}

function loadComponent(component: new (container: HTMLElement) => Component, selector: string): void {
  const containers: HTMLElement[] = Array.from(document.querySelectorAll(selector));

  containers.forEach(container => {
    new component(container);
  });
}
