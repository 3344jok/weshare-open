import request from '../utils/request';

export const fetchData = query => {
    return request({
        url: './api/group/table',
        method: 'get',
        params: query
    });
};

export const fetchAlbumsData = query => {
    return request({
        url: './api/album/table',
        method: 'get',
        params: query
    });
};

