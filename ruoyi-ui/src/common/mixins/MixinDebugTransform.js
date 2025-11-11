export default {
  data() {
    return {
      perspective: "10000px",
      rotateX: 0,
      rotateY: 0,
      degPixel: 0.2
    };
  },
  computed: {
    sylDebugTransform() {
      return {
        transform: `perspective(${this.perspective}) rotateX(${this.rotateX}deg) rotateY(${this.rotateY}deg) rotateZ(0deg)`
      };
    }
  },
  methods: {
    handleDebugTransform(e) {
      e.stopPropagation();
      if (e.which !== 3) return false;
      const start = {
        x: e.clientX,
        y: e.clientY
      };
      const fnMove = em => {
        const x = em.clientX;
        const y = em.clientY;
        this.rotateX += (start.y - y) * this.degPixel;
        this.rotateY -= (start.x - x) * this.degPixel;
        this.rotateX = Math.min(Math.max(this.rotateX, -88), 88);
        this.rotateY = Math.min(Math.max(this.rotateY, -88), 88);
        start.x = x;
        start.y = y;
      };
      const fnUp = () => {
        document.removeEventListener("mousemove", fnMove);
        document.removeEventListener("mouseup", fnUp);
      };
      document.addEventListener("mousemove", fnMove);
      document.addEventListener("mouseup", fnUp);
    }
  }
};