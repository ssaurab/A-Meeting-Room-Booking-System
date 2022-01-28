import { roomsReducer ,RoomsState} from '../src/app/admin/rooms/rooms.reducer'
import { roomsActions } from '../src/app/admin/rooms/rooms.actions'


describe('Room reducer', () => {
    let initial_state: RoomsState;
    beforeAll(() => {
        initial_state = {
            loading: true,
            showError: false,
            rooms: [],
        }
    });
    it('should update error', () => {
        expect(roomsReducer(undefined, {
            type: "UPDATE_ROOMSSHOWERROR" as const,
            payload: { showError: true },
        })).toEqual({ ...initial_state, showError: true })
    })
    it('should update loading state', () => {
        expect(roomsReducer(initial_state, {
            type: "UPDATE_ROOMS_LOADING" as const,
            payload: { loading: false }
        })).toEqual({ ...initial_state, loading: false })
    });
})