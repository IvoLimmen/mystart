export class Link {
  id: number;
  description: string;
  title: string;
  url: string;
  labels: string[];
}

export class Label {
  label: string;
  count: number;
}

export class User {
  id: number;
  autoStartLabel: string;
  avatarFileName: string;
  email: string;
  fullName: string;
  menuLabels: Array<string>;
  openNewTab: boolean;
}
