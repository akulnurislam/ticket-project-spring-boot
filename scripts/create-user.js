import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  vus: 4500,
  iterations: 4500,
  rps: 150,
  duration: '30s',
};

export default function() {
  const headers = {
    'Content-Type': 'application/json',
  };
  const body = JSON.stringify({
    username: `user_${__VU}`,
  });

  const response = http.post('http://localhost:8080/users', body, { headers });

  check(response, {
    'status is 201': (r) => r.status === 201,
    'status is not 201': (r) => r.status !== 201,
  });

  if (response.status !== 201) {
    console.log(response.body);
  }

  sleep(1);
}
