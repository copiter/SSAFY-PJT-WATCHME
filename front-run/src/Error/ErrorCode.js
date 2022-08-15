function ErrorCode(result) {
  const code = result.code;

  if (code === 596) {
    alert(`에러코드 ${code} : ${result.message}`);
    return;
  }

  switch (code) {
    case 300:
      alert(`에러코드 ${code} : 허용되지 않습니다`);
      break;
    case 500:
      alert(`에러코드 ${code} : 내부 서버 에러`);
      break;
    case 501:
      alert(`에러코드 ${code} : 로그인을 해주세요`);
      break;
    case 502:
      alert(`에러코드 ${code} : 로그인 실패`);
      break;
    case 503:
      alert(`에러코드 ${code} : 멤버가 아닙니다`);
      break;
    case 504:
      alert(`에러코드 ${code} : 없는 멤버입니다`);
      break;
    case 505:
      alert(`에러코드 ${code} : 이메일 키가 옳지 않습니다`);
      break;
    case 506:
      alert(`에러코드 ${code} : 이메일 키가 만료되었습니다`);
      break;
    case 509:
      alert(`에러코드 ${code} : 이미 회원가입 되어 있습니다`);
      break;
    case 510:
      alert(`에러코드 ${code} : 해당 그룹이 없습니다`);
      break;
    case 511:
      alert(`에러코드 ${code} : 카테고리가 없습니다`);
      break;
    case 512:
      alert(`에러코드 ${code} : 파일 업로드에 실패했습니다`);
      break;
    case 513:
      alert(`에러코드 ${code} : 패스워드가 틀렸습니다`);
      break;
    case 514:
      alert(`에러코드 ${code} : 중복된 이메일입니다`);
      break;
    case 515:
      alert(`에러코드 ${code} : 중복된 닉네임입니다`);
      break;
    case 516:
      alert(`에러코드 ${code} : 비밀번호 길이가 맞지 않습니다`);
      break;
    case 520:
      alert(`에러코드 ${code} : 관련 정보가 없습니다`);
      break;
    case 521:
      alert(`에러코드 ${code} : 허용되지 않는 값입니다`);
      break;
    case 522:
      alert(`에러코드 ${code} : 없는 방입니다`);
      break;
    case 523:
      alert(`에러코드 ${code} : 참가자가 아닙니다`);
      break;
    case 524:
      alert(`에러코드 ${code} : 같은 이름이 존재합니다`);
      break;
    case 525:
      alert(`에러코드 ${code} : 비밀번호가 숫자가 아닙니다`);
      break;
    case 526:
      alert(`에러코드 ${code} : 허용되지 않는 인원수입니다`);
      break;
    case 527:
      alert(`에러코드 ${code} : 허용되지 않는 종료기간입니다`);
      break;
    case 528:
      alert(`에러코드 ${code} : 이미지 파일이 아닙니다`);
      break;
    case 529:
      alert(`에러코드 ${code} : 이미지 파일의 크기는 10MB 이하여야 합니다`);
      break;
    case 530:
      alert(`에러코드 ${code} : 방장이 아닙니다`);
      break;
    case 531:
      alert(`에러코드 ${code} : 스터디 로그 생성을 실패했습니다`);
      break;
    case 532:
      alert(`에러코드 ${code} : 스터디 로그 저장을 실패했습니다`);
      break;
    case 533:
      alert(`에러코드 ${code} : 스프린트가 없습니다`);
      break;
    case 534:
      alert(`에러코드 ${code} : 이미 참가되었습니다`);
      break;
    case 535:
      alert(`에러코드 ${code} : 내가 참여한 그룹이 아닙니다`);
      break;
    case 536:
      alert(`에러코드 ${code} : 방장이 아닙니다`);
      break;
    case 537:
      alert(`에러코드 ${code} : 모집중인 스프린트가 아닙니다`);
      break;
    case 538:
      alert(`에러코드 ${code} : 포인트가 부족합니다`);
      break;
    case 540:
      alert(`에러코드 ${code} : 참가 신청하지 않았습니다`);
      break;
    case 541:
      alert(`에러코드 ${code} : 진행중인 스프린트가 아닙니다`);
      break;
    case 543:
      alert(`에러코드 ${code} : 스프린트가 이미 시작되었습니다`);
      break;
    case 544:
      alert(`에러코드 ${code} : 허용되는 시작/종료일자가 아닙니다`);
      break;
    case 550:
      alert(`에러코드 ${code} : 인원 초과입니다`);
      break;
    case 551:
      alert(`에러코드 ${code} : 비밀번호는 숫자만 가능합니다`);
      break;
    case 552:
      alert(`에러코드 ${code} : 참가신청이 반려되었습니다`);
      break;
    case 564:
      alert(`에러코드 ${code} : 비밀번호는 숫자만 가능합니다`);
      break;
    case 565:
      alert(`에러코드 ${code} : 허용되지 않습니다`);
      break;
    case 566:
      alert(`에러코드 ${code} : 반려되었습니다`);
      break;
    case 567:
      alert(`에러코드 ${code} : 이미 신청되었습니다`);
      break;
    case 595:
      alert(`에러코드 ${code} : 비정상적인 접근입니다`);
      break;
    case 597:
      alert(`에러코드 ${code} : 카카오페이 오류입니다`);
      break;
    case 598:
      alert(`에러코드 ${code} : 허용되지 않는 포인트 값입니다`);
      break;
    case 599:
      alert(`에러코드 ${code} : 날짜 파싱을 실패했습니다`);
      break;
  }
}

export default ErrorCode;
