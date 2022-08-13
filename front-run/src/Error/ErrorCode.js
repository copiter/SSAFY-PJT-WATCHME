function ErrorCode(result) {
  let msg = "에러코드가 없습니다. 관리자에게 문의하세요.";

  alert(result);

  switch (num) {
    case 300:
      alert(`에러코드 ${num} : 허용되지 않습니다`);
      break;
    case 500:
      alert(`에러코드 ${num} : 내부 서버 에러`);
      break;
    case 501:
      alert(`에러코드 ${num} : JWT 토큰 에러`);
      break;
    case 502:
      alert(`에러코드 ${num} : 로그인 실패`);
      break;
    case 503:
      alert(`에러코드 ${num} : 멤버가 아닙니다`);
      break;
    case 504:
      alert(`에러코드 ${num} : 없는 멤버입니다`);
      break;
    case 505:
      alert(`에러코드 ${num} : 이메일 키가 옳지 않습니다`);
      break;
    case 506:
      alert(`에러코드 ${num} : 이메일 키가 만료되었습니다`);
      break;
    case 509:
      alert(`에러코드 ${num} : 이미 회원가입 되어 있습니다`);
      break;
    case 510:
      alert(`에러코드 ${num} : 해당 그룹이 없습니다`);
      break;
    case 511:
      alert(`에러코드 ${num} : 카테고리가 없습니다`);
      break;
    case 512:
      alert(`에러코드 ${num} : 파일 업로드에 실패했습니다`);
      break;
    case 513:
      alert(`에러코드 ${num} : 패스워드가 틀렸습니다`);
      break;
    case 514:
      alert(`에러코드 ${num} : 중복된 이메일입니다`);
      break;
    case 515:
      alert(`에러코드 ${num} : 중복된 닉네임입니다`);
      break;
    case 520:
      alert(`에러코드 ${num} : 정보를 찾을 수 없습니다`);
      break;
    case 521:
      alert(`에러코드 ${num} : 허용되지 않는 값입니다`);
      break;
    case 522:
      alert(`에러코드 ${num} : 없는 방입니다`);
      break;
    case 523:
      alert(`에러코드 ${num} : 참가자가 아닙니다`);
      break;
    case 524:
      alert(`에러코드 ${num} : 같은 이름이 존재합니다`);
      break;
    case 525:
      alert(`에러코드 ${num} : 비밀번호가 숫자가 아닙니다`);
      break;
    case 526:
      alert(`에러코드 ${num} : 허용되지 않는 인원수입니다`);
      break;
    case 527:
      alert(`에러코드 ${num} : 허용되지 않는 종료기간입니다`);
      break;
    case 528:
      alert(`에러코드 ${num} : 이미지 파일이 아닙니다`);
      break;
    case 529:
      alert(`에러코드 ${num} : 이미지 파일의 크기가 너무 큽니다`);
      break;
    case 530:
      alert(`에러코드 ${num} : 방장이 아닙니다`);
      break;
    case 531:
      alert(`에러코드 ${num} : 스터디 로그 생성을 실패했습니다`);
      break;
    case 532:
      alert(`에러코드 ${num} : 스터디 로그 저장을 실패했습니다`);
      break;
    case 533:
      alert(`에러코드 ${num} : 스프린트가 없습니다`);
      break;
    case 534:
      alert(`에러코드 ${num} : 이미 참가되었습니다`);
      break;
    case 535:
      alert(`에러코드 ${num} : 내가 참여한 그룹이 아닙니다`);
      break;
    case 536:
      alert(`에러코드 ${num} : 방장이 아닙니다`);
      break;
    case 537:
      alert(`에러코드 ${num} : 모집중인 스프린트가 아닙니다`);
      break;
    case 538:
      alert(`에러코드 ${num} : 포인트가 부족합니다`);
      break;
    case 540:
      alert(`에러코드 ${num} : 참가 신청하지 않았습니다`);
      break;
    case 541:
      alert(`에러코드 ${num} : 진행중인 스프린트가 아닙니다`);
      break;
    case 543:
      alert(`에러코드 ${num} : 스프린트가 이미 시작되었습니다`);
      break;
    case 544:
      alert(`에러코드 ${num} : 허용되는 시작/종료일자가 아닙니다`);
      break;
    case 550:
      alert(`에러코드 ${num} : 인원 초과입니다`);
      break;
    case 551:
      alert(`에러코드 ${num} : 비밀번호는 숫자만 가능합니다`);
      break;
    case 552:
      alert(`에러코드 ${num} : 참가신청이 반려되었습니다`);
      break;
    case 564:
      alert(`에러코드 ${num} : 비밀번호는 숫자만 가능합니다`);
      break;
    case 565:
      alert(`에러코드 ${num} : 허용되지 않습니다`);
      break;
    case 566:
      alert(`에러코드 ${num} : 반려되었습니다`);
      break;
    case 567:
      alert(`에러코드 ${num} : 이미 신청되었습니다`);
      break;
    case 597:
      alert(`에러코드 ${num} : 카카오페이 오류입니다`);
      break;
    case 598:
      alert(`에러코드 ${num} : 허용되지 않는 포인트 값입니다`);
      break;
    case 599:
      alert(`에러코드 ${num} : 날짜 파싱을 실패했습니다`);
      break;
  }
}

export default ErrorCode;
