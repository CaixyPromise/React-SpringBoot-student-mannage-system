declare namespace Subject {
  type CurrentRow = {
    id?: string,
    name?: string,
    gradeCredit: number;
    gradeMax?: number;
    gradeMin?: number;
    gradeFail?: number | undefined;
    gradeExcellent?: number | undefined;
  }

  type SubjectFormItem = CurrentRow & {
    gradeExcellentType?: number;
    gradeFailType?: number;
  }
}
