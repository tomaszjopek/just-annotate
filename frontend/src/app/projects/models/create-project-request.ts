enum ProjectType {
  TEXT
}

interface Label {
  name: string;
  color: string;
}

export interface CreateProjectRequest {
  name: string;
  description?: string;
  type: ProjectType;
  labels: Label[]
}
