import React from "react";

import "./Signup.css";

function Signup() {
  return (
    <>
        <div id = 'signup'>
            <from>
                <div id='signup-top'>
                    <div id='signup-top-title'>SIGN UP</div>
                </div>
                <div id="signup-left">
                    <div id='signup-left-image'></div>
                    <button  id='signup-left-addimage'>프로필 사진 추가</button>
                </div>
                <div id="signup-right">
                    <div id="signup-right-1">
                        <input class="width70 input" type="email" placeholder="이메일을 입력하세요"/>
                        <button class = 'dup'>중복확인</button>
                    </div>
                    <div id="signup-right-2">
                        <input class="width45 input" type="password" placeholder="비밀번호를 입력하세요"/>
                        <input class="width45 left10 input" type="password" placeholder="비밀번호를 다시한번 입력하세요"/>
                    </div>
                    <div id="signup-right-3"><input class="width100 input" type="text" placeholder="이름을 입력하세요"/></div>
                    <div id="signup-right-4">
                        <input class="width70 input" type="text" placeholder="별명을 입력하세요"/>
                        <button class = 'dup'>중복확인</button>
                    </div>
                    <div id="signup-right-5">
                        <select class="width100 input"name="country" placeholder="성별을 입력하세요">
                            <option value="M">남</option>
                            <option value="W">녀</option>
                            <option value="N" selected>공개안함</option>
                         </select>
                    </div>
                    <div id="signup-right-6"><input class="width100 input" type="date" placeholder="생년월일을 입력하세요"/></div>
                    <div id="signup-right-7"><input class="width100 input" type="number" placeholder="전화번호를 입력하세요"/></div>
                    <div id="signup-right-8">
                        <input class="width100 input submitting" type="submit" value="회원가입"/>
                    </div>
                </div>
            </from>
        </div>
    </>
  );
}

export default Signup;
