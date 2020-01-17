export class Magazine {
    public id: number;
    public name: string;
    public issn: string;
    public isOpenAcces: boolean;
    public editors: string;
    public reviewers: string;
    public active: boolean;

    constructor(_id: number, _name: string, _issn: string, _isOpenAcces: boolean, _editors: string, _reviewers: string, _active: boolean) {
        this.id = _id;
        this.name = _name;
        this.issn = _issn;
        this.isOpenAcces = _isOpenAcces;
        this.editors = _editors;
        this.reviewers = _reviewers;
        this.active = _active;
    }
}
