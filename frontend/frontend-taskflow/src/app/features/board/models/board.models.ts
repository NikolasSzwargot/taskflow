export type StatusDto = {
    id: string;
    name: string;
    position: number;
};

export type TaskDto = {
    id: string;
    title: string;
    description: string | null;
    statusId: string;
    position: number;
};
