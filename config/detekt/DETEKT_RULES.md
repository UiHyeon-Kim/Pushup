### LongMethod
- ViewModel이 비대해지는 문제 방지
- 함수가 너무 커지면 분리해 SRP 원칙 준수

### LongParameterList
- UiState 없이 Composable 에 넣는 것 방지
- 관련된 기능들은 데이터 클래스로 묶어 사용

### TooManyFunctions
- ViewModel에 책임이 너무 많아지지 않도록 함
- 화면 분리 또는 UseCase 분리

### CyclomaticComplexMethod
- 상태 분기가 너무 많아지지 않도록 방지

### MagicNumber
- 의미 없는 숫자 사용 금지
- 센서 임계값이나 시간값을 상수로 관리하도록 유도

### ReturnCount
- 함수 흐름 단순화를 위해
- early return 남용 방지

### UnusedPrivateMember
- 사용하지 않는 private 변수나 함수 방지

### ExpressionBodySyntax
- 평소에도 자주 쓰는 규칙으로 한 줄 함수는 단일 표현식 사용

### MaxLineLength
- Kotlin Code Convention 규칙으로 120자 제한

### FunctionNaming
- Composable 제외 카멜케이스 적용

### VariableNaming
- 카멜케이스, 숫자는 뒤에 작성
- Boolean 변수는 is / has / can / should 등 접두사 (강제 x)
- 상수는 전체 대문자 + 공백은 _ 사용
- _는 private 만 허용

### SpreadOperator
- 잘 안쓰긴 하지만 가변 인자 * 사용 제한

### ComposableNaming
- 컴포즈 함수는 대문자로 시작

### MutableParams
- Mutable 타입 전달 금지
