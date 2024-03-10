import http from 'k6/http';
import {check, sleep} from 'k6';

export const options = {
    vus: 4500,
    iterations: 4500,
    rps: 150,
    duration: '30s',
};

export default function () {
    const headers = {
        'Content-Type': 'application/json',
        'Authorization': `user_${__VU}`,
    };
    const body = JSON.stringify({
        quantity: 1,
        ticket_id: 'aed78802-5c75-4714-8500-a971a0c9a210',
    });

    const response = http.post('http://localhost:8080/bookings', body, {headers});

    check(response, {
        'success booking': (r) => r.status === 201,
        'sold out': (r) => r.status === 409,
        'error': (r) => r.status !== 201 && r.status !== 409,
    });

    sleep(1);
}
