export interface response {
  code: number;
  data: data;
}

export interface data {
  offset: number;
  limit: number;
  count: number;
  total: number;
  results: hero[];
}

export interface hero {
  id: number;
  name: string;
  description: string;
  thumbnail: thumbnail;
}

export interface thumbnail {
  extension: string;
  path: string;
}

export interface search {
  heroName: string;
  offset: number;
}

export interface heroComments {
  id: number;
  comments: string[];
}
