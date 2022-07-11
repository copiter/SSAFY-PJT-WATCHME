You already know about 'State Hook'
import React, { useState } from 'react';

function Example() {
  // "count"라는 새 상태 변수를 선언합니다
  const [count, setCount] = useState(0);

  return (
    <div>
      <p>You clicked {count} times</p>
      <button onClick={() => setCount(count + 1)}>
        Click me
      </button>
    </div>
  );
}
버튼을 클릭하면 값이 증가하는, React를 공부한다면 많이 작성해봤을 예제 코드이다. 여기서 useState가 바로 Hook이다! 위 예제에서 useState 는 Array를 제공하는데, 첫번째는 Data, 두번째는 Modify Function을 준다. 이 Modify Function을 이용해서 state 값을 바꿀 경우 component도 동시에 리렌더링된다!

여기서 Modify Function을 이용해서 state 값을 바꾸는 방법은 두가지가 있는데,

직접 값 입력하기 : 이전 state를 이용해서 현재 state를 바꿔주는 것

setCounter(counter + 1);
함수를 전달하기 : 함수형태 : 함수가 뭘 return하던지 이것이 새 state를 반환한다!

setCounter((current) => current+1)
React가 확실히 현재 값이라는 것을 보장하고 있어!
예상치 못한 업데이트가 어디선가 일어났어도 혼동을 주는 것을 방지
state 변수를 선언할 때 class를 사용하면 this.state를 이용해 사용했다.

 class Example extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      count: 0
    };
  }
state 변수 선언
함수 component는 this를 가질 수 없어 this.state를 할당하거나 읽을 수 없다. 대신 useState Hook을 직접 component에 호출한다.

import React, { useState } from 'react';

function Example() {
  // 새로운 state 변수를 선언하고, 이것을 count라 부르겠습니다.
  const [count, setCount] = useState(0);
state 가져오기
class component는 this.state.count로 사용하지만 함수 component는 count로 사용이 가능하다.

state 갱신하기
클래스 컴포넌트는 count를 갱신하기 위해 this.setState()를 호출한다.

반면 함수 컴포넌트는 setCount와 count 변수를 가지고 있으므로 this를 호출하지 않아도 된다.

그래서 Hook이 뭐라구요?
Hook은 Function component에서 React state와 생명주기(Lifecycle features)을 연동(hook into) 할 수 있게 해주는 JavaScript 함수이다. Hook은 class 안에서는 동작하지 않는다. 대신 함수형으로 React를 사용할 수 있게 해준다. 두 스타일을 비교하고 싶다면 React class vs function style - 3.2. 함수에서 state 사용법 hook : 생활코딩을 참고하면 된다. hook이 등장하면서 함수 스타일로 클래스 스타일의 기능성에 버금가는 component를 만들 수 있게되었다.

React는 useState같은 내장 Hook을 몇 가지 제공하고 또한 사용자 정의 Hook도 만들 수 있다! 일단 내장 Hook들인 Effect Hook과 State Hook을 소개하고, Hook 규칙을 숙지하고 나만의 Hook을 만들어보겠다!

Effect Hook
React component 안에서 Data를 가져오거나(get) 구독하고(subscribe), DOM을 직접 조작하는 작업의 모든 동작을 **side effects(effects)**라고 한다. 왜냐면 이런 동작들은 다른 component에 영향을 줄 수도 있고, 렌더링 과정에서는 구현할 수 없는 작업이기 때문이다!

useEffect는 function component 내에서 이런 side effect를 수행할 수 있게 해준다. React class의 componentDidMount, componentDidUpdate, componentWillUnmount와 같은 목적이지만 하나의 API로 통합 된것이 useEffect이다. 비교하는 자료가 궁금하다면 Using the Effect Hook

Hook을 사용하면 구독을 추가하고 제거하는 로직과 같이 서로 관련있는 코드들을 한군데에 모아서 작성할 수 있다.

Effect Hook 예시 : DOM 업데이트 후 문서의 타이틀을 바꾸는 component
import React, { useState, useEffect } from 'react';

function Example() {
  const [count, setCount] = useState(0);

  // componentDidMount, componentDidUpdate와 비슷합니다
  useEffect(() => {
    // 브라우저 API를 이용해 문서의 타이틀을 업데이트합니다
    document.title = `You clicked ${count} times`;
  });

  return (
    <div>
      <p>You clicked {count} times</p>
      <button onClick={() => setCount(count + 1)}>
        Click me
      </button>
    </div>
  );
}
React는 첫 렌더링 포함 매 렌더링 이후에 effects를 실행한다. useEffect를 사용하면 Dom을 바꾼 이후에 effect 함수를 실행 할 것이다. Effect는 component 안에 있어서 props와 state에 접근이 가능하다.

Effect Hook 예시 : 친구의 접속 상태를 구독하는 effect, 구독 해지시 effect 해제
import React, { useState, useEffect } from 'react';

function FriendStatus(props) {
  const [isOnline, setIsOnline] = useState(null);

  function handleStatusChange(status) {
    setIsOnline(status.isOnline);
  }

  useEffect(() => {
    ChatAPI.subscribeToFriendStatus(props.friend.id, handleStatusChange);
    return () => {
      ChatAPI.unsubscribeFromFriendStatus(props.friend.id, handleStatusChange);
    };
  });

  if (isOnline === null) {
    return 'Loading...';
  }
  return isOnline ? 'Online' : 'Offline';
}
위 예시에서 component가 unmount될 때 React가 ChatAPI에서 구독을 해지 할 것이다. 또한 리렌더링이 일어나 effect를 재실행하기 전에도 구독을 해지한다.

React component는 일반적으로 두종류의 side effect가 있다. 정리가 필요한 것과 필요없는 것. 어떻게 구분할까?

정리(clean-up)를 이용하지 않는 Effects
React가 DOM을 업데이트한 뒤 추가로 코드를 실행해야 하는 경우.네트워크 request, DOM 수동 조작, Loging 등은 정리가 필요 없는 것들이다.

class를 이용한 예시
class Example extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      count: 0
    };
  }

  componentDidMount() {
    document.title = `You clicked ${this.state.count} times`;
  }
  componentDidUpdate() {
    document.title = `You clicked ${this.state.count} times`;
  }

  render() {
    return (
      <div>
        <p>You clicked {this.state.count} times</p>
        <button onClick={() => this.setState({ count: this.state.count + 1 })}>
          Click me
        </button>
      </div>
    );
  }
}
class 안의 두 개의 생명주기 메서드에 같은 코드가 중복된다. 개념적으로 렌더링 이후 항상 같은 코드가 수행되길 바래서 이렇게 작성한다. React class component는 렌더링 이후 항상 같은 코드가 되길 바라는 메서드가 없다.

Hook을 이용하는 예시
import React, { useState, useEffect } from 'react';

function Example() {
  const [count, setCount] = useState(0);

  useEffect(() => {
    document.title = `You clicked ${count} times`;
  });

  return (
    <div>
      <p>You clicked {count} times</p>
      <button onClick={() => setCount(count + 1)}>
        Click me
      </button>
    </div>
  );
}
아까 본 코드다. useEffect를 이용해 React에게 component가 렌더링 된 이후에 어떤 일을 수행할 지 말해준다. React는 이 함수(effect)를 기억했다가 DOM 업데이트를 수행한 이후에 불러낼 것이다.

Hook 사용 규칙
두 가지 규칙을 준수해야 하는데,

최상위에서만 Hook을 호출해야한다. 즉 반복문, 조건문, 중첩된 함수 내에서 Hook을 실행하면 안된다!
React함수 component 내에서만 Hook을 호출해야 한다. 일반 JS 함수에서는 Hook을 호출하면 안된다.(Hook은 custon Hook에서만 호출이 가능하다..!)
나만의 Hook 만들기
상태 관련 로직을 component 간에 재사용 하고싶을 때가 있을 것이다. 이를 해결하기 위해서 higher-order components와 render props가 있다. Custom Hook은 위 둘과 달리 component tree에 새 component를 추가하지 않고도 가능하게 해준다.

Custom Hook은 기능이라기보단 convention에 가깝다. 이름이 use로 시작하고 안에서 다른 Hook을 호출하는 것은 Custom Hook이라고 부를 수 있다. 폼 핸들링, 애니메이션, 선언적 구독(declarative subscriptions), 타이머 등 많은 경우에 custom Hook을 사용할 수 있다.

import React, { useState, useEffect } from 'react';

function useFriendStatus(friendID) {
  const [isOnline, setIsOnline] = useState(null);

  function handleStatusChange(status) {
    setIsOnline(status.isOnline);
  }

  useEffect(() => {
    ChatAPI.subscribeToFriendStatus(friendID, handleStatusChange);
    return () => {
      ChatAPI.unsubscribeFromFriendStatus(friendID, handleStatusChange);
    };
  });

  return isOnline;
}
위와 같이 useFriendStatus라는 함수로 custom Hook을 뽑아낸다! 이 Hook은 friendID를 argument로 받아서 친구의 접속상태를 반환해준다. 이제 이 custom Hook을 여러 component에서 사용이 가능하다.

function FriendStatus(props) {
  const isOnline = useFriendStatus(props.friend.id);

  if (isOnline === null) {
    return 'Loading...';
  }
  return isOnline ? 'Online' : 'Offline';
}
function FriendListItem(props) {
  const isOnline = useFriendStatus(props.friend.id);

  return (
    <li style={{ color: isOnline ? 'green' : 'black' }}>
      {props.friend.name}
    </li>
  );
}
여기서 각 component의 state는 완전히 독립적이다! Hook은 state 그 자체가 아니라, 상태 관련 로직을 재사용하는 방법이다. 실제로 각각 Hook 호출은 완전히 독립된 state를 가진다. 그래서 한 component 안에서 같은 custom Hook을 두 번 쓸 수도 있다.

다른 Hook들
useContext : component를 중첩하지 않고도 React context를 구독할 수 있다.
function Example() {
  const locale = useContext(LocaleContext);
  const theme = useContext(ThemeContext);
  // ...
}
useReducer : 복잡한 components들의 state를 reducer로 관리하게 해준다.
function Todos() {
  const [todos, dispatch] = useReducer(todosReducer);
  // ...
참고자료 : React 공식 문서
